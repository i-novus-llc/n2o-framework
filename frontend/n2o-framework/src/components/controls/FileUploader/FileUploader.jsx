import React from 'react'
import Dropzone from 'react-dropzone'
import cn from 'classnames'
import isEmpty from 'lodash/isEmpty'
import { Button } from 'reactstrap'

import FileUploaderList from './FileUploaderList'

function FileUploader({
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
}) {
    if (!visible) {
        return null
    }

    const showControl = multiple || (!multiple && isEmpty(files))

    return (
        <div
            className={`n2o-file-uploader-container ${componentClass}-container`}
        >
            <Dropzone
                className={cn('n2o-file-uploader-control', componentClass, {
                    'd-none': !showControl,
                    [className]: className,
                    [uploaderClass]: uploaderClass,
                    disabled,
                })}
                accept={accept}
                multiple={multiple}
                disabled={disabled}
                onDrop={onDrop}
                onDragEnter={onDragEnter}
                onDragLeave={onDragLeave}
                onDropRejected={onDropRejected}
            >
                {children}
            </Dropzone>
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
                    {t('save')}
                </Button>
            )}
        </div>
    )
}

FileUploader.defaultProps = {
    t: () => {},
}

export default FileUploader
