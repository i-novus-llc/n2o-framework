import { Severity } from "./IValidation"

/**
 * @enum VALIDATION_SEVERITY_PRIORITY Приоритет сообщений валидации по типу
 */
export const VALIDATION_SEVERITY_PRIORITY = {
    [Severity.danger]: 0,
    [Severity.warning]: 1,
    [Severity.success]: 2,
}
