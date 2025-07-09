import React, { ReactNode, FC, CSSProperties, useState, useCallback, useRef } from 'react'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'
import { Button } from 'reactstrap'

import { fileAccepted } from '../FileUploader/utils'

import { ImageUploaderList } from './ImageUploaderList'
import { type File as CustomFile } from './ImageUploaderItem'
import { type ImgError } from './utils'

export interface Props {
    uploading: Record<string, boolean>
    statusBarColor?: string
    onRemove(index: number, id: string): void
    autoUpload?: boolean
    showSize?: boolean
    showName?: boolean
    disabled?: boolean
    children?: ReactNode | FC
    onImagesDrop(
        acceptedFiles: File[],
        rejectedFiles: File[],
        event?: React.DragEvent<HTMLDivElement>): void
    onDragEnter?(): void
    onDragLeave?(): void
    multiple?: boolean
    visible?: boolean
    className?: string
    files: CustomFile[]
    componentClass?: string
    onStartUpload?(): void
    uploaderClass?: string
    saveBtnStyle?: CSSProperties
    lightbox?: boolean
    listType?: 'card' | 'image'
    imgError?: ImgError
    showTooltip?: boolean
    customUploaderSize?: CSSProperties
    canDelete?: boolean
    shape?: 'circle' | 'square'
    accept?: string
}

// TODO объеденить с FileUpload
export const ImageUpload = ({
    uploading,
    statusBarColor,
    onRemove,
    autoUpload,
    showSize,
    showName,
    disabled,
    children,
    onImagesDrop,
    onDragEnter,
    onDragLeave,
    multiple,
    visible,
    className,
    files,
    componentClass,
    onStartUpload,
    uploaderClass,
    saveBtnStyle,
    lightbox,
    listType,
    imgError,
    showTooltip,
    customUploaderSize,
    canDelete,
    shape,
    accept,
}: Props) => {
    const [dragActive, setDragActive] = useState(false)
    const fileInputRef = useRef<HTMLInputElement>(null)
    const dragCounter = useRef(0)

    const handleClick = useCallback(() => {
        if (disabled) { return }
        fileInputRef.current?.click()
    }, [disabled])

    const handleDragEnter = useCallback((e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault()
        e.stopPropagation()
        if (disabled) { return }

        dragCounter.current += 1
        setDragActive(true)
        if (onDragEnter) { onDragEnter() }
    }, [disabled, onDragEnter])

    const handleDragLeave = useCallback((e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault()
        e.stopPropagation()

        dragCounter.current -= 1
        if (dragCounter.current === 0) {
            setDragActive(false)
            if (onDragLeave) { onDragLeave() }
        }
    }, [onDragLeave])

    const handleDragOver = useCallback((e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault()
        e.stopPropagation()
        if (disabled) { return }
        e.dataTransfer.dropEffect = 'copy'
    }, [disabled])

    const processFiles = useCallback((
        files: File[],
        event?: React.DragEvent<HTMLDivElement>,
    ) => {
        const acceptedFiles: File[] = []
        const rejectedFiles: File[] = []

        files.forEach((file) => {
            if (fileAccepted(file, accept)) {
                acceptedFiles.push(file)
            } else {
                rejectedFiles.push(file)
            }
        })

        if (!multiple && acceptedFiles.length > 0) {
            rejectedFiles.unshift(...acceptedFiles.slice(1))
            acceptedFiles.splice(1)
        }

        if (onImagesDrop) {
            onImagesDrop(acceptedFiles, rejectedFiles, event)
        }
    }, [accept, multiple, onImagesDrop])

    const handleDrop = useCallback((e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault()
        e.stopPropagation()
        dragCounter.current = 0
        setDragActive(false)
        if (disabled) { return }

        const droppedFiles = Array.from(e.dataTransfer.files) as File[]

        processFiles(droppedFiles, e)
    }, [disabled, processFiles])

    const handleFileChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
        if (disabled) { return }
        const selectedFiles = e.target.files ? Array.from(e.target.files) as File[] : []

        processFiles(selectedFiles)
        e.target.value = ''
    }, [disabled, processFiles])

    if (!visible) { return null }

    const showControl = multiple || (!multiple && isEmpty(files))
    const defaultClassName = 'n2o-image-uploader-control'
    const compiledClassName = shape ? `${defaultClassName}--shape-${shape}` : defaultClassName

    const dropzoneClasses = classNames(
        'n2o-image-uploader-control',
        componentClass,
        compiledClassName,
        {
            'd-none': !showControl,
            [className || '']: className,
            [uploaderClass || '']: uploaderClass,
            'img-error': !isEmpty(imgError),
            'drag-active': dragActive,
            disabled,
        },
    )

    return (
        <div>
            <div className={classNames('n2o-image-uploader-container', { [`${componentClass}-container`]: componentClass })}>
                <input
                    type="file"
                    ref={fileInputRef}
                    accept={accept}
                    multiple={multiple}
                    disabled={disabled}
                    onChange={handleFileChange}
                    style={{ display: 'none' }}
                />
                <div
                    className={dropzoneClasses}
                    style={customUploaderSize}
                    onClick={handleClick}
                    onDragEnter={handleDragEnter}
                    onDragOver={handleDragOver}
                    onDragLeave={handleDragLeave}
                    onDrop={handleDrop}
                >
                    {children}
                </div>
                {!isEmpty(files) && (
                    <ImageUploaderList
                        files={files}
                        uploading={uploading}
                        statusBarColor={statusBarColor}
                        onRemove={onRemove}
                        autoUpload={autoUpload}
                        showSize={showSize}
                        showName={showName}
                        lightBox={lightbox}
                        disabled={disabled}
                        listType={listType}
                        customUploaderSize={customUploaderSize}
                        showTooltip={showTooltip}
                        canDelete={canDelete}
                        shape={shape}
                    />
                )}
            </div>
            {!autoUpload && (
                <Button className="n2o-drop-zone-save-btn" style={saveBtnStyle} onClick={onStartUpload}>Сохранить</Button>
            )}
        </div>
    )
}

export default ImageUpload
