package pl.miloszgilga.jargonaut.core.dto;

import java.util.List;

public record AlphabeticalGroup<T>(String letter, int totalItems, List<T> items) {
}
