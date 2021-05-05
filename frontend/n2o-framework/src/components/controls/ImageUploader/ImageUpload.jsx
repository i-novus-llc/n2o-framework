import React from 'react'
import PropTypes from 'prop-types'
import Dropzone from 'react-dropzone'
import cn from 'classnames'
import isEmpty from 'lodash/isEmpty'
import Button from 'reactstrap/lib/Button'

import ImageUploaderList from './ImageUploaderList'

function ImageUpload(props) {
    const {
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
    } = props
    const showControl = multiple || (!multiple && isEmpty(files))
    const componentClassContainer = `${componentClass}-container`

    if (!visible) {
        return null
    }

    return (
        <div>
            <div
                className={cn('n2o-image-uploader-container', {
                    [componentClassContainer]: componentClass,
                })}
            >
                {!disabled && (
                    <Dropzone
                        className={cn('n2o-image-uploader-control', componentClass, {
                            'd-none': !showControl,
                            [className]: className,
                            [uploaderClass]: uploaderClass,
                            'img-error': !isEmpty(imgError),
                            'n2o-image-uploader-control--shape-circle':
            shape === 'circle',
                        })}
                        style={customUploaderSize}
                        accept="image/*"
                        multiple={multiple}
                        disabled={disabled}
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
                <Button
                    className="n2o-drop-zone-save-btn"
                    style={saveBtnStyle}
                    onClick={onStartUpload}
                >
                    {'Сохранить'}
                </Button>
            )}
        </div>
    )
}

ImageUpload.propTypes = {
    uploading: PropTypes.object,
    statusBarColor: PropTypes.string,
    onRemove: PropTypes.func,
    autoUpload: PropTypes.bool,
    showSize: PropTypes.bool,
    showName: PropTypes.bool,
    disabled: PropTypes.bool,
    children: PropTypes.oneOfType([PropTypes.node, PropTypes.func]),
    onImagesDrop: PropTypes.func,
    onDragEnter: PropTypes.func,
    onDragLeave: PropTypes.func,
    multiple: PropTypes.bool,
    visible: PropTypes.bool,
    className: PropTypes.string,
    files: PropTypes.arrayOf(PropTypes.object),
    componentClass: PropTypes.string,
    onStartUpload: PropTypes.func,
    uploaderClass: PropTypes.string,
    saveBtnStyle: PropTypes.object,
    lightbox: PropTypes.bool,
    listType: PropTypes.string,
    imgError: PropTypes.string,
    showTooltip: PropTypes.bool,
    customUploaderSize: PropTypes.number,
    canDelete: PropTypes.bool,
    shape: PropTypes.string,
}

export default ImageUpload
