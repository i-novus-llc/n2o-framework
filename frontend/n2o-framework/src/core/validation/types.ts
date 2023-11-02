export enum Severity {
    danger = 'danger',
    warning = 'warning',
    success = 'success'
}

export enum ValidationTypes {
    condition = 'condition',
    email = 'email',
    required = 'required',
    constraint = 'constraint',
    integer = 'integer',
    minLength = 'minLength',
    maxLength = 'maxLength'
}

export enum ValidationsKey {
    Validations = 'validations',
    FilterValidations = 'filterValidations'
}

export interface ExtraValidationConfig {
    expression: string
    min: number
    max: number
    signal?: AbortSignal
}

export interface Validation extends ExtraValidationConfig {
    severity: Severity
    text: string
    type: ValidationTypes
    enablingConditions: string[]
    on: string[]
}

export interface ValidationResult {
    severity: Severity
    text: string
}

export type ValidateFunction = <
    TData extends object = object,
    TKey extends keyof TData = keyof TData
> (key: TKey, values: TData, config: ExtraValidationConfig) => boolean | Promise<boolean>
