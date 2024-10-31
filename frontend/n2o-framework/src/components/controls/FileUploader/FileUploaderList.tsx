import React from 'react'

import FileUploaderItem from './FileUploaderItem'

interface FileUploaderListProps {
    files: Array<{
        id: string
        name: string
        type: string
        size: number
        percentage?: number
    }>;
    onRemove(): void
    uploading?: Record<string, string>
}

function FileUploaderList({
    files,
    onRemove,
    uploading,
    ...rest
}: FileUploaderListProps) {
    return (
        <div className="n2o-file-uploader-files-list">
            {files.map((file, index) => (
                // @ts-ignore import from js file
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
