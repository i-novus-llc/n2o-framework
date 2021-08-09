import React from 'react'
import classNames from 'classnames'
import Progress from 'reactstrap/lib/Progress'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'

import { Tooltip } from '../../snippets/Tooltip/Tooltip'
import { Spinner } from '../../snippets/Spinner/Spinner'

import { convertSize } from './utils'

function FileUploaderItem(props) {
    const {
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
    } = props

    const error = (!isEmpty(file.error) || !isEmpty(file.response)) && (file.response || file.error)

    return (
        <div className="n2o-file-uploader-files-item">
            <span className="n2o-file-uploader-files-item-info">
                <Tooltip
                    label={(
                        <a
                            title={file.name}
                            href={file.link}
                            /* eslint-disable-next-line react/jsx-no-target-blank */
                            target="_blank"
                            id={`tooltip-${file.id}`}
                            className={classNames('n2o-file-uploader-link', {
                                'n2o-file-uploader-item-error': file.error,
                            })}
                        >
                            <span className="n2o-file-uploader-file-name">{file.name}</span>
                            {file.link && (
                                <i className=" n2o-file-uploader-external-link fa fa-external-link" />
                            )}
                        </a>
                    )}
                    hint={error}
                    placement="bottom"
                />
                <span className={classNames('n2o-file-uploader-item-size', { showSize })}>
                    {showSize && <span>{convertSize(file.size)}</span>}
                    {!disabled && !loading && (
                        <i
                            onClick={() => onRemove(index, file.id)}
                            className={classNames('n2o-file-uploader-remove ml-2', deleteIcon || 'fa fa-times')}
                        />
                    )}
                    {loading && <Spinner className="ml-2" type="inline" size="sm" />}
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

FileUploaderItem.propTypes = {
    file: PropTypes.object,
    percentage: PropTypes.number,
    statusBarColor: PropTypes.string,
    deleteIcon: PropTypes.string,
    onRemove: PropTypes.func,
    showSize: PropTypes.bool,
    disabled: PropTypes.bool,
    autoUpload: PropTypes.bool,
    index: PropTypes.number,
    loading: PropTypes.bool,
}

FileUploaderItem.defaultProps = {
    statusBarColor: 'success',
}

export default FileUploaderItem
