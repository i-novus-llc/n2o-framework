import React from 'react';

import map from 'lodash/map';

import ImageUploaderItem from './ImageUploaderItem';
import PropTypes from 'prop-types';

class ImageUploaderList extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    const { files, onRemove, uploading, imgFiles, ...rest } = this.props;
    return (
      <>
        {map(files, (file, index) => {
          return (
            <ImageUploaderItem
              file={file}
              key={index}
              percentage={file.percentage}
              onRemove={onRemove}
              index={index}
              loading={uploading && uploading[file.id]}
              src={imgFiles[index]}
              {...rest}
            />
          );
        })}
      </>
    );
  }
}

ImageUploaderList.propTypes = {
  files: PropTypes.arrayOf(PropTypes.object),
  percentage: PropTypes.number,
  onRemove: PropTypes.func,
};

export default ImageUploaderList;
