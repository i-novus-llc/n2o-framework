import { FileItem } from '../../../../controls/FileUploader/types'

export interface Props {
    multi?: boolean
    files: FileItem[]
    t(text: string): string
    showSize: boolean
    label: string,
    uploadIcon: string
    deleteIcon: string
    className: string
    disabled: boolean
    onRemove(index: number, id: string): void
}
