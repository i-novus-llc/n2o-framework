import React from 'react';
import FileUploaderItem from './FileUploaderItem';
import PropTypes from 'prop-types';

class FileUploaderList extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    const { files, onRemove, uploading, ...rest } = this.props;
    return (
      <div className="n2o-file-uploader-files-list">
        {files.map((file, index) => {
          return (
            <FileUploaderItem
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
      </div>
    );
  }
}

FileUploaderList.propTypes = {
  files: PropTypes.arrayOf(PropTypes.object),
  percentage: PropTypes.number,
  onRemove: PropTypes.func,
};

export default FileUploaderList;
