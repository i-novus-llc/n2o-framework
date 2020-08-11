import React from 'react';

import isEmpty from 'lodash/isEmpty';

import withFileUploader from '../FileUploader/withFileUploader';
import PropTypes from 'prop-types';
import ImageUploader from './ImageUploader';

function defaultDropZone(icon, label) {
  return (
    <React.Fragment>
      <div className={icon} />
      {label}
    </React.Fragment>
  );
}

function DropZone(props) {
  const { icon, label, children, imgError } = props;
  const currentLabel = !isEmpty(imgError) ? imgError.message : label;
  return (
    <ImageUploader
      {...props}
      children={children || defaultDropZone(icon, currentLabel)}
      componentClass={'n2o-drop-zone'}
    />
  );
}

DropZone.defaultProps = {
  saveBtnStyle: {
    marginTop: '10px',
  },
};

DropZone.propTypes = {
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

export default withFileUploader(DropZone);
