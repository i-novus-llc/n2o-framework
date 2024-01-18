import { ValidationResult } from '../validation/types'

export function getValidationClass(message?: ValidationResult): string | false {
    if (!message) {
        return false
    }

    if (message.severity === 'success') {
        return 'is-valid'
    }

    if (message.severity === 'warning') {
        return 'has-warning'
    }

    return 'is-invalid'
}
