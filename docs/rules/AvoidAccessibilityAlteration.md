
# AvoidAccessibilityAlteration
**Category:** `pmd`<br/>
**Rule Key:** `pmd:AvoidAccessibilityAlteration`<br/>


-----

Methods such as getDeclaredConstructors(), getDeclaredConstructor(Class[]) and setAccessible(), as the interface PrivilegedAction, allow to alter, at runtime, the visilibilty of variable, classes, or methods, even if they are private. Obviously, no one should do so, as such behavior is against everything encapsulation principal stands for.
