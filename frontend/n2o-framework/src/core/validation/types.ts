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

export interface ExtraValidationConfig {
    expression: string
    min: number
    max: number
    signal?: AbortSignal
    datasourceId?: string
    pageId?: string
}

export interface Validation extends ExtraValidationConfig {
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
    config: Validation & {
        expression: string;
        min: number;
        max: number;
        datasourceId: string | undefined;
        pageUrl?: string | null;
        signal: AbortSignal | undefined;
    }) => boolean | Promise<boolean | ValidationResult>
