import React from 'react'
import classNames from 'classnames'
import { Progress } from 'reactstrap'
import isEmpty from 'lodash/isEmpty'

import { Tooltip } from '../../snippets/Tooltip/TooltipHOC'
import { Spinner } from '../../snippets/Spinner/Spinner'

import { convertSize } from './utils'
import { File } from './File'

function FileUploaderItem({
    file,
    percentage,
    statusBarColor,
    onRemove,
    showSize,
    disabled,
    index,
    loading,
    autoUpload,
    deleteIcon,
}) {
    const { response, error, status, size, id, name, link } = file

    const hint = (!isEmpty(response) || !isEmpty(error)) && (response || error)
    const inProgress = loading || (!autoUpload && !status)

    return (
        <div className="n2o-file-uploader-files-item">
            <span className="n2o-file-uploader-files-item-info">
                <Tooltip hint={hint} placement="bottom">
                    <File name={name} link={link} id={id} error={error} />
                </Tooltip>
                <span className={classNames('n2o-file-uploader-item-size', { showSize })}>
                    {showSize && <span className="ml-2 n2o-file-uploader__size">{convertSize(size)}</span>}
                    {!disabled && (
                        <i
                            onClick={() => onRemove(index, id)}
                            className={classNames('n2o-file-uploader-remove ml-2', deleteIcon || 'fa fa-times')}
                        />
                    )}
                    {loading && <Spinner type="inline" size="sm" />}
                </span>
            </span>
            {inProgress && (
                <Progress className="n2o-file-uploader-progress-bar" value={percentage} color={statusBarColor} animated />
            )}
        </div>
    )
}

FileUploaderItem.defaultProps = {
    statusBarColor: 'success',
}

export default FileUploaderItem
