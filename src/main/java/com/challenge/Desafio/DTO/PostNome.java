package com.challenge.Desafio.DTO;

import jakarta.validation.constraints.NotNull;

public record PostNome(
        @NotNull(message = "Nome é obrigatório")
        String Nome
) {}
