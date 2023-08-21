export type SetValue = (fieldName: string, value: unknown) => void
export type SetFocus = (fieldName: string) => void
export type SetBlur = (fieldName: string) => void
export type GetValues = <T = unknown>(fieldName: string | string[]) => T
