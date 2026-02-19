# Contributing to MiningLevels

## Development Environment

### Prerequisites

- Java 21 (JDK)
- Maven 3.x
- Git

### Setup

```bash
git clone https://github.com/ChafficPlugins/MiningLevels.git
cd MiningLevels
mvn clean verify
```

## Branch Naming

- `feature/<description>` — new features
- `fix/<description>` — bug fixes
- `chore/<description>` — maintenance, dependency updates

## Testing

All changes must pass `mvn clean verify` with 0 test failures before merging.

### Writing Tests

- Test files go in `src/test/java/` mirroring the main source structure
- Use JUnit 5 (`@Test`, `@BeforeAll`, `@BeforeEach`, `@AfterAll`)
- Use `MockBukkit.mock()` for tests needing Bukkit API (Materials, ItemStacks, etc.)
- Do **not** use `MockBukkit.load(MiningLevels.class)` — the plugin depends on CrucialLib at runtime
- Always call `MockBukkit.unmock()` in `@AfterAll`
- Clear static registries in `@BeforeEach` (e.g., `MiningLevel.miningLevels.clear()`)

### Test Structure

```java
class MyTest {
    @BeforeAll
    static void setUpServer() {
        MockBukkit.mock();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @BeforeEach
    void setUp() {
        // Clear and set up test data
    }

    @Test
    void methodName_condition_expectedBehavior() {
        // Arrange, Act, Assert
    }
}
```

## PR Checklist

- [ ] `mvn clean verify` passes with 0 failures
- [ ] New code has unit tests where applicable
- [ ] No secrets or credentials committed
- [ ] Commit messages are clear and descriptive
- [ ] `plugin.yml` version updated if releasing
