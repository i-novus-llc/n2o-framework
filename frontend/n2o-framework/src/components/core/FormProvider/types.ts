import { Model } from '../../../core/models/types'
import { ValidationResult } from '../../../core/validation/types'

export type SetValue = (fieldName: string, value: unknown) => void
export type SetFocus = (fieldName: string) => void
export type SetBlur = (fieldName: string) => void
export type SetMessage = (fieldName: string, message: ValidationResult | null) => void
export type GetValues = <T = unknown, P extends string | undefined = undefined>(fieldName?: P) => P extends string ? T : Model
