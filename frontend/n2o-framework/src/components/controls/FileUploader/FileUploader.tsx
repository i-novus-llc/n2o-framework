import React, { useState, useCallback, useRef } from 'react'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'
import { Button } from 'reactstrap'

import FileUploaderList from './FileUploaderList'
import { type FileUploaderProps } from './types'
import { fileAccepted } from './utils'

// TODO объеденить с ImageUpload
export const FileUploader = ({
    accept,
    uploading,
    statusBarColor,
    onRemove,
    autoUpload,
    showSize,
    disabled,
    children,
    onDrop,
    onDropRejected,
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
    deleteIcon,
    t,
}: FileUploaderProps) => {
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

    const processFiles = useCallback((files: File[]) => {
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

        if (acceptedFiles.length > 0 && onDrop) {
            onDrop(acceptedFiles, rejectedFiles)
        }

        if (rejectedFiles.length > 0 && onDropRejected) {
            onDropRejected(rejectedFiles)
        }
    }, [accept, multiple, onDrop, onDropRejected])

    const handleDrop = useCallback((e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault()
        e.stopPropagation()
        dragCounter.current = 0
        setDragActive(false)
        if (disabled) { return }

        const droppedFiles = Array.from(e.dataTransfer.files)

        processFiles(droppedFiles)
    }, [disabled, processFiles])

    const handleFileChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
        if (disabled) { return }
        const selectedFiles = e.target.files ? Array.from(e.target.files) : []

        processFiles(selectedFiles)
        e.target.value = ''
    }, [disabled, processFiles])

    if (!visible) { return null }

    const showControl = multiple || (!multiple && isEmpty(files))
    const dropzoneClasses = classNames(
        'n2o-file-uploader-control',
        componentClass,
        className,
        uploaderClass,
        {
            'd-none': !showControl,
            disabled,
            'drag-active': dragActive,
        },
    )

    return (
        <div className={`n2o-file-uploader-container ${componentClass}-container`}>
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
                onClick={handleClick}
                onDragEnter={handleDragEnter}
                onDragOver={handleDragOver}
                onDragLeave={handleDragLeave}
                onDrop={handleDrop}
            >
                {children}
            </div>
            {!isEmpty(files) && (
                <FileUploaderList
                    files={files}
                    uploading={uploading}
                    statusBarColor={statusBarColor}
                    onRemove={onRemove}
                    autoUpload={autoUpload}
                    showSize={showSize}
                    disabled={disabled}
                    deleteIcon={deleteIcon}
                />
            )}
            {!autoUpload && (
                <Button
                    className="n2o-drop-zone-save-btn"
                    style={saveBtnStyle}
                    onClick={onStartUpload}
                >
                    {t?.('save')}
                </Button>
            )}
        </div>
    )
}

export default FileUploader
