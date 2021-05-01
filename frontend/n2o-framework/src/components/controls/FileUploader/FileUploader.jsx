import React from 'react'
import Dropzone from 'react-dropzone'
import cn from 'classnames'
import isEmpty from 'lodash/isEmpty'
import Button from 'reactstrap/lib/Button'

import FileUploaderList from './FileUploaderList'

class FileUploader extends React.Component {
    render() {
        const {
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
            t,
        } = this.props
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
}

FileUploader.defaultProps = {
    t: () => {},
}

export default FileUploader
