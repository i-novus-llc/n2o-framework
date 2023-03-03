export type TSetValue = (fieldName: string, value: unknown) => void
export type TSetFocus = (fieldName: string) => void
export type TSetBlur = (fieldName: string) => void
export type TReset = <T extends object>(data: T) => void
export type TGetValues = <T = unknown>(fieldName: string | string[]) => T
