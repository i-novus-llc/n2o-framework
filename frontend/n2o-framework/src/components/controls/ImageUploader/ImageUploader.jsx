import React from 'react';
import PropTypes from 'prop-types';

import isEmpty from 'lodash/isEmpty';

import withFileUploader from '../FileUploader/withFileUploader';
import ImageUpload from './ImageUpload';

function defaultDropZone(icon, label) {
  return (
    <React.Fragment>
      <div className={icon} />
      {label}
    </React.Fragment>
  );
}

function ImageUploader(props) {
  const { icon, label, children, imgError } = props;
  const currentLabel = !isEmpty(imgError) ? imgError.message : label;
  return (
    <ImageUpload
      {...props}
      children={children || defaultDropZone(icon, currentLabel)}
      componentClass={'n2o-drop-zone'}
    />
  );
}

ImageUploader.defaultProps = {
  saveBtnStyle: {
    marginTop: '10px',
  },
};

ImageUploader.propTypes = {
  label: PropTypes.string,
  uploading: PropTypes.object,
  icon: PropTypes.string,
  files: PropTypes.arrayOf(PropTypes.object),
  className: PropTypes.string,
  onDrop: PropTypes.func,
  autoUpload: PropTypes.bool,
  onRemove: PropTypes.func,
  onStartUpload: PropTypes.func,
  saveBtnStyle: PropTypes.object,
  visible: PropTypes.bool,
  disabled: PropTypes.bool,
  requestParam: PropTypes.string,
  maxSize: PropTypes.number,
  minSize: PropTypes.number,
  multiple: PropTypes.bool,
  onChange: PropTypes.func,
  children: PropTypes.oneOfType([PropTypes.node, PropTypes.func]),
};

export default withFileUploader(ImageUploader);
