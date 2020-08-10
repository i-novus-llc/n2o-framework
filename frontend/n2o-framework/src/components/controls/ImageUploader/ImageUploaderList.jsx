import React from 'react';
import ImageUploaderItem from './ImageUploaderItem';
import PropTypes from 'prop-types';

class ImageUploaderList extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    const { files, onRemove, uploading, ...rest } = this.props;
    return (
      <>
        {files.map((file, index) => {
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
