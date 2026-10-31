package pl.miloszgilga.jargonaut.asset;

import pl.miloszgilga.jargonaut.asset.converter.AssetFileConverter;

public record AssetTarget(String shortPath, String fullPath, AssetFileConverter assetFileConverter) {
}
