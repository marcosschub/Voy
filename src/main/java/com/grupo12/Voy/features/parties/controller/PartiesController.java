package com.grupo12.Voy.features.parties.controller;

import com.grupo12.Voy.features.parties.Dto.PartyReqDTO;
import com.grupo12.Voy.features.parties.Dto.PartyReqPrivateDto;
import com.grupo12.Voy.features.parties.Dto.PartyResDTO;
import com.grupo12.Voy.features.parties.service.IPartyService;
import com.grupo12.Voy.features.tags.dto.TagsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parties")
@Tag(name = "Parties", description = "Gestión de eventos")
public class PartiesController {

    private final IPartyService partyService;

    @Operation(summary = "Listar eventos públicos", description = """
            Retorna una lista de eventos públicos filtrados por los parámetros opcionales.
            No requiere autenticación.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de eventos encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PartyResDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PartyResDTO>> findAll(
            @RequestParam(required = false) UUID partyId,
            @RequestParam(required = false) UUID organizerId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city
    ) {
        return ResponseEntity.ok(partyService.getAll(partyId, organizerId, title, city));
    }

    @Operation(summary = "Obtener evento por ID", description = """
            Retorna el detalle de un evento por su externalId.
            Los eventos privados solo son visibles para su organizador o un ADMIN.
            El ID del usuario autenticado se obtiene del token JWT para aplicar el filtro de visibilidad.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento encontrado",
                    content = @Content(schema = @Schema(implementation = PartyResDTO.class))),
            @ApiResponse(responseCode = "403", description = "Sin acceso al evento", content = @Content),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PartyResDTO> findById(
            @PathVariable UUID id,
            @AuthenticationPrincipal(expression = "usuario.externalId") UUID currentUserId
    ) {
        return ResponseEntity.ok(partyService.getById(id, currentUserId));
    }

    @Operation(summary = "Crear evento público", description = """
            Crea un nuevo evento público. Requiere rol ORGANIZATOR.
            El organizador se asigna automáticamente desde el token JWT.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evento creado exitosamente",
                    content = @Content(schema = @Schema(implementation = PartyResDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos para crear eventos públicos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Ya existe un evento con ese título", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/public")
    @PreAuthorize("hasRole('ORGANIZATOR')")
    public ResponseEntity<PartyResDTO> createPublic(@RequestBody @Valid PartyReqDTO party,
                                                    @AuthenticationPrincipal(expression = "usuario.externalId") UUID currentUserId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.createPublic(party, currentUserId));
    }

    @Operation(summary = "Crear evento privado", description = """
            Crea un nuevo evento privado con un máximo de 30 invitados. Requiere rol USER.
            El organizador se asigna automáticamente desde el token JWT.
            El precio se establece en 0 automáticamente.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evento privado creado exitosamente",
                    content = @Content(schema = @Schema(implementation = PartyResDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Ya existe un evento con ese título", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/private")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PartyResDTO> createPrivate(@RequestBody @Valid PartyReqPrivateDto party,
                                                     @AuthenticationPrincipal(expression = "usuario.externalId") UUID currentUserId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.createPrivate(party, currentUserId));
    }

    @Operation(summary = "Actualizar evento", description = """
            Actualiza los datos de un evento existente. Requiere rol ORGANIZATOR o USER.
            Si el evento es privado, el precio se mantiene en 0.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = PartyResDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos para actualizar", content = @Content),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATOR') or hasRole('USER')")
    public ResponseEntity<PartyResDTO> update(@PathVariable UUID id, @RequestBody @Valid PartyReqDTO party) {
        return ResponseEntity.ok(partyService.update(id, party));
    }

    @Operation(summary = "Agregar etiqueta a evento", description = """
            Agrega una etiqueta existente a un evento.
            La etiqueta no puede repetirse en el mismo evento.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Etiqueta agregada exitosamente",
                    content = @Content(schema = @Schema(implementation = PartyResDTO.class))),
            @ApiResponse(responseCode = "404", description = "Evento o etiqueta no encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "La etiqueta ya está asignada al evento", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/{id}/tag")
    public ResponseEntity<PartyResDTO> addTag(@PathVariable UUID id, @RequestBody @Valid TagsDTO tagsDTO) {
        return ResponseEntity.ok(partyService.addTag(id, tagsDTO));
    }

    @Operation(summary = "Eliminar evento", description = """
            Realiza una baja lógica del evento (no lo elimina de la base de datos).
            Requiere rol ORGANIZATOR o ADMIN.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos para eliminar", content = @Content),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        partyService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Eliminar etiqueta de evento", description = """
        Elimina una etiqueta asignada a un evento.
        Si la etiqueta no está asignada al evento se retorna 404.
        """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Etiqueta eliminada exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Evento o etiqueta no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}/tag")
    public ResponseEntity<Void> removeTag(@PathVariable UUID id, @RequestBody @Valid TagsDTO tagsDTO) {
        partyService.removeTag(id, tagsDTO);
        return ResponseEntity.noContent().build();
    }
}