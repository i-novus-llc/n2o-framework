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

export type ValidateFunction = (
    key: string,
    values: Record<string, unknown>,
    config: {
        severity: Severity;
        expression: string;
        min: number;
        enablingConditions: string[];
        max: number;
        datasourceId: string | undefined;
        text: string;
        type: ValidationTypes;
        pageUrl?: string | null;
        signal: AbortSignal | undefined;
        on: string[]
    }) => boolean | Promise<boolean | ValidationResult>
