import React from 'react'
import PropTypes from 'prop-types'

import FileUploaderItem from './FileUploaderItem'

class FileUploaderList extends React.Component {
    constructor(props) {
        super(props)
    }

    render() {
        const { files, onRemove, uploading, ...rest } = this.props
        return (
            <div className="n2o-file-uploader-files-list">
                {files.map((file, index) => (
                    <FileUploaderItem
                        file={file}
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
}

FileUploaderList.propTypes = {
    files: PropTypes.arrayOf(PropTypes.object),
    percentage: PropTypes.number,
    onRemove: PropTypes.func,
}

export default FileUploaderList
