import React from 'react';

import Dropzone from 'react-dropzone';

import cn from 'classnames';

import isEmpty from 'lodash/isEmpty';

import Button from 'reactstrap/lib/Button';
import ImageUploaderList from './ImageUploaderList';

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
    imgFiles,
    componentClass,
    onStartUpload,
    uploaderClass,
    saveBtnStyle,
    lightbox,
    listType,
    imgError,
  } = props;
  const showControl = multiple || (!multiple && isEmpty(files));
  const componentClassContainer = `${componentClass}-container`;
  return (
    <React.Fragment>
      {visible && (
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
                })}
                accept={'image/*'}
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
                imgFiles={imgFiles}
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

export default ImageUpload;
