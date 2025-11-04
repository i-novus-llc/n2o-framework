import { CSSProperties, ReactNode } from 'react'
import { CancelTokenSource, AxiosResponse } from 'axios'

export interface FileItem {
    id: string
    name: string
    fileName: string
    type: string
    size: number
    percentage: number
    response: string
    error: string
    status: string
    link: string
    cancelSource?: CancelTokenSource
}

interface CommonProps {
    onRemove(index: number, id: string): void
    statusBarColor?: string
    autoUpload?: boolean
    showSize?: boolean
    disabled?: boolean
    deleteIcon?: ReactNode | string
}

export interface FileUploaderItemProps extends CommonProps {
    file: FileItem
    percentage?: number
    index: number
    loading?: boolean
    statusBarColor?: string
    autoUpload?: boolean
    showSize?: boolean
    disabled?: boolean
    deleteIcon?: ReactNode | string
}

export interface FileUploaderListProps extends CommonProps {
    files?: FileItem[]
    uploading?: Record<string, boolean>
}

export interface FileUploaderProps extends FileUploaderListProps {
    accept?: string
    children?: ReactNode
    onDrop?(acceptedFiles: File[], rejectedFiles: File[]): void
    onDropRejected?(rejectedFiles: File[]): void
    onDragEnter?(): void
    onDragLeave?(): void
    multiple?: boolean
    visible?: boolean
    className?: string
    componentClass?: string
    onStartUpload?(): void
    uploaderClass?: string
    saveBtnStyle?: CSSProperties
    t?(key: string): string
}

export type Files = FileItem[]

export interface FileUploaderControlProps {
    files?: FileUploaderListProps['files']
    value?: Files
    model?: Record<string, unknown>
    fieldKey: string
    mapper?(files?: Files): Files
    accept?: string
    multi?: boolean
    autoUpload?: boolean
    onChange(files?: FileUploaderListProps['files'] | FileItem | null): void
    onBlur(files?: FileUploaderListProps['files'] | null): void
    onStart(file: FileItem): void
    onSuccess(response: AxiosResponse): void
    onDelete(index: number, id: string): void
    deleteUrl?: string
    deleteRequest?(id: string): void
    requestParam: string
    uploadUrl: string
    valueFieldId: string
    labelFieldId: string
    statusFieldId: string
    sizeFieldId: string
    responseFieldId: string
    urlFieldId: string
    t?(key: string): string
    visible?: boolean
    icon?: string
    statusBarColor?: string
    disabled?: boolean
    showSize?: boolean
    propsResolver(expression: string, model?: Record<string, unknown>): string
    onError(error: { message: string, status?: string | number }): void
    uploadRequest(
        formData: FormData,
        onProgress: (event: ProgressEvent<EventTarget>) => void,
        onUpload: (response: AxiosResponse<unknown, unknown>) => void,
        onError: (error: { message: string, status: string | number }) => void
    ): void
}

export type FileItemKeys = {
    valueFieldId: keyof FileItem;
    labelFieldId: keyof FileItem;
    statusFieldId: keyof FileItem;
    sizeFieldId: keyof FileItem;
    responseFieldId: keyof FileItem;
    urlFieldId: keyof FileItem;
}

export interface FileUploaderControlState {
    files: Files;
    imgFiles: Files;
    imgError: Record<string, unknown>;
    uploading?: FileUploaderListProps['uploading'];
    uploaderClass?: string | null;
}
