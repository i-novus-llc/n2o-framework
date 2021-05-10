import React from 'react'
import PropTypes from 'prop-types'
import map from 'lodash/map'

import ImageUploaderItem from './ImageUploaderItem'

function ImageUploaderList(props) {
    const {
        files,
        onRemove,
        uploading,
        imgFiles,
        showTooltip,
        canDelete,
        ...rest
    } = props

    return map(files, (file, index) => (
        <ImageUploaderItem
            file={file}
            key={index}
            percentage={file.percentage}
            onRemove={onRemove}
            index={index}
            loading={uploading && uploading[file.id]}
            showTooltip={showTooltip}
            canDelete={canDelete}
            {...rest}
        />
    ))
}

ImageUploaderList.propTypes = {
    files: PropTypes.arrayOf(PropTypes.object),
    percentage: PropTypes.number,
    onRemove: PropTypes.func,
}

export default ImageUploaderList
