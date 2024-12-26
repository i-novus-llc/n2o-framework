import React from 'react'

import FileUploaderItem from './FileUploaderItem'
import { type FileUploaderListProps } from './types'

function FileUploaderList({
    onRemove,
    uploading,
    files = [],
    ...rest
}: FileUploaderListProps) {
    return (
        <div className="n2o-file-uploader-files-list">
            {files?.map((file, index) => (
                <FileUploaderItem
                     /* eslint-disable-next-line react/no-array-index-key */
                    key={index}
                    file={file}
                    percentage={file.percentage}
                    onRemove={onRemove}
                    index={index}
                    loading={uploading?.[file.id]}
                    {...rest}
                />
            ))}
        </div>
    )
}

export default FileUploaderList
