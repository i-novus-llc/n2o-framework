export enum Severity {
    danger = 'danger',
    warning = 'warning',
    success = 'success',
}

export enum ValidationTypes {
    condition = 'condition',
    email = 'email',
    required = 'required',
    constraint = 'constraint',
    integer = 'integer',
    minLength = 'minLength',
    maxLength = 'maxLength',
}

export enum ValidationsKey {
    Validations = 'validations',
    FilterValidations = 'filterValidations',
}

export type ExtraValidationConfig = Partial<{
    expression: string
    min: number
    max: number
    signal: AbortSignal
    datasourceId: string
    pageId: string
    pageUrl: string
}>

export interface Validation {
    enablingConditions: string[]
    on: RegExp[]
    severity: Severity
    text: string
    type: ValidationTypes
    validationKey?: string
}

export interface ValidationResult {
    severity: Severity
    text: string
}

export type ValidateFunction = (
    key: string,
    values: Record<string, unknown>,
    config: Validation & ExtraValidationConfig) => boolean | Promise<boolean | ValidationResult>
