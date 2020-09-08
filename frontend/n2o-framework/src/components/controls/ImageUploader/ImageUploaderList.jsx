import React from 'react';
import PropTypes from 'prop-types';

import map from 'lodash/map';

import ImageUploaderItem from './ImageUploaderItem';

function ImageUploaderList(props) {
  const { files, onRemove, uploading, imgFiles, ...rest } = props;

  return map(files, (file, index) => {
    return (
      <ImageUploaderItem
        file={file}
        key={index}
        percentage={file.percentage}
        onRemove={onRemove}
        index={index}
        loading={uploading && uploading[file.id]}
        {...rest}
      />
    );
  });
}

ImageUploaderList.propTypes = {
  files: PropTypes.arrayOf(PropTypes.object),
  percentage: PropTypes.number,
  onRemove: PropTypes.func,
};

export default ImageUploaderList;
