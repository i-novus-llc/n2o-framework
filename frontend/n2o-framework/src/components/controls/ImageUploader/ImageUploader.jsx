import React from 'react';

import Dropzone from 'react-dropzone';

import cn from 'classnames';

import isEmpty from 'lodash/isEmpty';

import Button from 'reactstrap/lib/Button';
import ImageUploaderList from './ImageUploaderList';

function ImageUploader({ ...rest }) {
  const {
    accept,
    uploading,
    statusBarColor,
    onRemove,
    autoUpload,
    showSize,
    showName,
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
  } = rest;
  const showControl = multiple || (!multiple && isEmpty(files));
  return (
    <React.Fragment>
      {visible && (
        <div>
          <div
            className={`n2o-image-uploader-container ${componentClass}-container`}
          >
            {!disabled && (
              <Dropzone
                className={cn('n2o-image-uploader-control', componentClass, {
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
              <ImageUploaderList
                files={files}
                uploading={uploading}
                statusBarColor={statusBarColor}
                onRemove={onRemove}
                autoUpload={autoUpload}
                showSize={showSize}
                showName={showName}
                disabled={disabled}
              />
            )}
          </div>
          {!autoUpload && (
            <Button
              className={'n2o-drop-zone-save-btn'}
              style={saveBtnStyle}
              onClick={onStartUpload}
            >
              Сохранить
            </Button>
          )}
        </div>
      )}
    </React.Fragment>
  );
}

export default ImageUploader;
