export interface Props {
    multi?: boolean
    files: File[]
    t(text: string): string
    showSize: boolean
    label: string,
    uploadIcon: string
    deleteIcon: string
    className: string
    disabled: boolean
}
