import React from 'react'
import Dropzone from 'react-dropzone'
import cn from 'classnames'
import isEmpty from 'lodash/isEmpty'
import Button from 'reactstrap/lib/Button'
import PropTypes from 'prop-types'

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
    const showControl = multiple || (!multiple && isEmpty(files))

    return (
        <>
            {visible && (
                <div
                    className={`n2o-file-uploader-container ${componentClass}-container`}
                >
                    {!disabled && (
                        <Dropzone
                            className={cn('n2o-file-uploader-control', componentClass, {
                                'd-none': !showControl,
                                [className]: className,
                                [uploaderClass]: uploaderClass,
                            })}
                            accept={accept}
                            multiple={multiple}
                            disabled={disabled}
                            onDrop={onDrop}
                            onDragEnter={onDragEnter}
                            onDragLeave={onDragLeave}
                        >
                            {children}
                        </Dropzone>
                    )}
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
            )}
        </>
    )
}

FileUploader.defaultProps = {
    t: () => {},
}

FileUploader.propTypes = {
    saveBtnStyle: PropTypes.any,
    uploaderClass: PropTypes.any,
    componentClass: PropTypes.any,
    files: PropTypes.any,
    statusBarColor: PropTypes.any,
    uploading: PropTypes.any,
    showSize: PropTypes.any,
    t: PropTypes.func,
    onDrop: PropTypes.func,
    onDragLeave: PropTypes.func,
    onStartUpload: PropTypes.func,
    onDragEnter: PropTypes.func,
    onRemove: PropTypes.func,
    accept: PropTypes.string,
    className: PropTypes.string,
    deleteIcon: PropTypes.string,
    autoUpload: PropTypes.bool,
    disabled: PropTypes.bool,
    multiple: PropTypes.bool,
    visible: PropTypes.bool,
    children: PropTypes.any,
}

export default FileUploader
