import React from 'react'
import PropTypes from 'prop-types'

import FileUploaderItem from './FileUploaderItem'

function FileUploaderList({ files, onRemove, uploading, ...rest }) {
    return (
        <div className="n2o-file-uploader-files-list">
            {files.map((file, index) => (
                <FileUploaderItem
                    file={file}
                    /* eslint-disable-next-line react/no-array-index-key */
                    key={index}
                    percentage={file.percentage}
                    onRemove={onRemove}
                    index={index}
                    loading={uploading && uploading[file.id]}
                    {...rest}
                />
            ))}
        </div>
    )
}

FileUploaderList.propTypes = {
    files: PropTypes.arrayOf(PropTypes.object),
    uploading: PropTypes.arrayOf(PropTypes.string),
    percentage: PropTypes.number,
    onRemove: PropTypes.func,
}

export default FileUploaderList
