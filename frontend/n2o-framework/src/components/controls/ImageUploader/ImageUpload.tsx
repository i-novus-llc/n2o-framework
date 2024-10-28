import React, { ReactNode, FC, CSSProperties } from 'react'
import Dropzone, { DropFilesEventHandler } from 'react-dropzone'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'
import { Button } from 'reactstrap'

import { ImageUploaderList } from './ImageUploaderList'
import { type File } from './ImageUploaderItem'
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
    onImagesDrop: DropFilesEventHandler
    onDragEnter?(): void
    onDragLeave?(): void
    multiple?: boolean
    visible?: boolean
    className?: string
    files: File[]
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
    const showControl = multiple || (!multiple && isEmpty(files))
    const componentClassContainer = `${componentClass}-container`

    if (!visible) { return null }

    const defaultClassName = 'n2o-image-uploader-control'
    const compiledClassName = shape ? `${defaultClassName}--shape-${shape}` : defaultClassName

    return (
        <div>
            <div className={classNames('n2o-image-uploader-container', { [componentClassContainer]: componentClass })}>
                {visible && (
                    <Dropzone
                        className={classNames('n2o-image-uploader-control', componentClass, compiledClassName, {
                            'd-none': !showControl,
                            className,
                            uploaderClass,
                            'img-error': !isEmpty(imgError),
                        })}
                        style={customUploaderSize}
                        accept={accept}
                        multiple={multiple}
                        disabled={disabled}
                        disabledClassName="disabled"
                        onDrop={onImagesDrop}
                        onDragEnter={onDragEnter}
                        onDragLeave={onDragLeave}
                    >
                        {children}
                    </Dropzone>
                )}
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
