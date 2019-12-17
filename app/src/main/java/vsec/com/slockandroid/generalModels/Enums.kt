package vsec.com.slockandroid.generalModels

enum class PasswordScore {
    WEAK, AVERAGE, STRONG, MARVELOUS
}

enum class ButtonState {
    EMAIL_VALID,
    EMAIL_EQUAL,
    FIRST_NAME_VALID,
    LAST_NAME_VALID,
    PASSWORD_VALID,
    PASSWORD_EQUAL,
    LOCK_NAME_VALID,
    LOCK_DESCRIPTION_VALID,
    REGISTERABLE_LOCK_FOUND
}

