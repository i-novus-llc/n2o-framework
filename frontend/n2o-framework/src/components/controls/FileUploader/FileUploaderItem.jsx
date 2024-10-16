import React from 'react'
import classNames from 'classnames'
import { Progress } from 'reactstrap'
import isEmpty from 'lodash/isEmpty'

import { Tooltip } from '../../snippets/Tooltip/TooltipHOC'
import { Spinner } from '../../snippets/Spinner/Spinner'

import { convertSize } from './utils'

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
    const error = (!isEmpty(file.error) || !isEmpty(file.response)) && (file.response || file.error)

    // eslint-disable-next-line react/no-unstable-nested-components
    const Component = ({ forwardedRef }) => (
        <a
            title={file.name}
            href={file.link}
            /* eslint-disable-next-line react/jsx-no-target-blank */
            target="_blank"
            id={`tooltip-${file.id}`}
            className={classNames('n2o-file-uploader-link', {
                'n2o-file-uploader-item-error': file.error,
            })}
            rel="noreferrer"
        >
            <span ref={forwardedRef} className="n2o-file-uploader-file-name">{file.name}</span>
            {file.link && (
                <i className=" n2o-file-uploader-external-link fa fa-external-link" />
            )}
        </a>
    )

    return (
        <div className="n2o-file-uploader-files-item">
            <span className="n2o-file-uploader-files-item-info">
                <Tooltip hint={error} placement="bottom"><Component /></Tooltip>
                <span className={classNames('n2o-file-uploader-item-size', { showSize })}>
                    {showSize && <span className="ml-2 n2o-file-uploader__size">{convertSize(file.size)}</span>}
                    {!disabled && (
                        <i
                            onClick={() => onRemove(index, file.id)}
                            className={classNames('n2o-file-uploader-remove ml-2', deleteIcon || 'fa fa-times')}
                        />
                    )}
                    {loading && <Spinner type="inline" size="sm" />}
                </span>
            </span>
            {loading ||
          (!autoUpload && !file.status && (
              <Progress
                  className="n2o-file-uploader-progress-bar"
                  value={percentage}
                  animated
                  color={statusBarColor}
              />
          ))}
        </div>
    )
}

FileUploaderItem.defaultProps = {
    statusBarColor: 'success',
}

export default FileUploaderItem
