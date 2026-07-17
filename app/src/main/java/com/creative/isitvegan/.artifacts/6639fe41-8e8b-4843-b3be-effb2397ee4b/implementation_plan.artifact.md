# Codebase Improvement & Refactoring Plan

Refactor the project to strictly follow Clean Architecture principles, centralize business logic (vegan status), and optimize data handling.

## Proposed Changes

### 1. Domain Layer Refinement
Create pure Kotlin models that are independent of any framework (Room/Retrofit).

#### [NEW] [Product.kt](file:///D:/Android/Projects/IsItVegan/app/src/main/java/com/creative/isitvegan/domain/model/Product.kt)
- Create a clean `Product` data class.
- Include a `Ingredient` domain model.
- Add extension properties for vegan logic: `isVegan`, `isNonVegan`, `statusText`, `statusColor`.

### 2. Data Layer Refactoring
Clean up the data layer and fix "leaky" abstractions.

#### [MODIFY] [ProductEntity.kt](file:///D:/Android/Projects/IsItVegan/app/src/main/java/com/creative/isitvegan/data/local/entity/ProductEntity.kt)
- Change Primary Key to `barcode` (from `_id` in API).
- Add `timestamp` field for sorting recent searches.
- Refactor `ingredients` to use a non-DTO list if possible (or keep as is but map to domain).

#### [MODIFY] [ProductMapper.kt](file:///D:/Android/Projects/IsItVegan/app/src/main/java/com/creative/isitvegan/data/mapper/ProductMapper.kt)
- Consolidate all mapping logic here (remove `Parser.kt`).
- Map `ProductDetails` (DTO) -> `Product` (Domain).
- Map `ProductEntity` (Entity) -> `Product` (Domain).
- Map `Product` (Domain) -> `ProductEntity` (Entity).

#### [MODIFY] [Repository.kt](file:///D:/Android/Projects/IsItVegan/app/src/main/java/com/creative/isitvegan/domain/repo/Repository.kt)
- Update all methods to return/accept `Product` (Domain) instead of DTOs or Entities.

#### [MODIFY] [RepositoryImpl.kt](file:///D:/Android/Projects/IsItVegan/app/src/main/java/com/creative/isitvegan/data/repo/RepositoryImpl.kt)
- Implement mapping logic using `ProductMapper` within the repository methods.

### 3. Presentation Layer Updates
Update ViewModels and Composables to work with Domain models.

#### [MODIFY] [ProductViewModel.kt](file:///D:/Android/Projects/IsItVegan/app/src/main/java/com/creative/isitvegan/ui/viewmodels/ProductViewModel.kt)
- Use `Product` (Domain) in `ProductUiState`.

#### [MODIFY] [SimpleRecentProductItem.kt](file:///D:/Android/Projects/IsItVegan/app/src/main/java/com/creative/isitvegan/ui/components/SimpleRecentProductItem.kt)
- Use Domain model properties for vegan logic instead of manual string parsing.

#### [DELETE] [Parser.kt](file:///D:/Android/Projects/IsItVegan/app/src/main/java/com/creative/isitvegan/ui/utils/Parser.kt)
- Logic moved to `ProductMapper`.

## Verification Plan

### Automated Tests
- Run existing unit tests: `./gradlew :app:testDebugUnitTest`.
- Update `ProductViewModelTest` and `RecentViewModelTest` to use Domain models.

### Manual Verification
- Deploy to device/emulator.
- Scan a product and verify vegan status display.
- Check "Recents" list to ensure items are saved and sorted correctly by timestamp.
