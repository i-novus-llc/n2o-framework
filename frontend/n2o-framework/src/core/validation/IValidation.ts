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

export interface IExtraValidationConfig {
    expression: string
    min: number
    max: number
}

export interface IValidation extends IExtraValidationConfig {
    severity: Severity
    text: string
    type: ValidationTypes
    enablingConditions: string[]
}

export interface IValidationResult {
    severity: Severity
    text: string
}

export type IValidateFunction = <
    TData extends object = object,
    TKey extends keyof TData = keyof TData
> (key: TKey, values: TData, config: IExtraValidationConfig) => boolean | Promise<boolean>
