# Refactoring Tasks

- [x] Create Domain Models
    - [x] `Ingredient` domain model
    - [x] `Product` domain model with vegan logic
- [x] Update Data Layer
    - [x] Update `ProductEntity` (PK = barcode, add `timestamp`)
    - [x] Refactor `ProductMapper` to handle all conversions
    - [x] Update `Repository` interface to use Domain models
    - [x] Update `RepositoryImpl` with mapping logic
- [x] Update Presentation Layer
    - [x] Update `ProductViewModel`
    - [x] Update `RecentViewModel`
    - [x] Update `ScanItemViewModel`
    - [x] Update `RecentSearchViewModel` (if applicable)
    - [x] Update UI components (e.g., `SimpleRecentProductItem`)
- [x] Cleanup & Verification
    - [x] Delete `Parser.kt`
    - [x] Run build and verify functionality
    - [x] Run unit tests
