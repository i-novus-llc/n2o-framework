import React from 'react'
import Tooltip from 'reactstrap/lib/Tooltip'
import cn from 'classnames'
import Progress from 'reactstrap/lib/Progress'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'

import Spinner from '../../snippets/Spinner/Spinner'

import { convertSize } from './utils'

class FileUploaderItem extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            tooltipOpen: false,
        }

        this.toggle = this.toggle.bind(this)
    }

    toggle() {
        this.setState({
            tooltipOpen: !this.state.tooltipOpen,
        })
    }

    render() {
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
        } = this.props

        return (
            <div className="n2o-file-uploader-files-item">
                <span className="n2o-file-uploader-files-item-info">
                    <a
                        title={file.name}
                        href={file.link}
                        target="_blank"
                        id={`tooltip-${file.id}`}
                        className={cn('n2o-file-uploader-link', {
                            'n2o-file-uploader-item-error': file.error,
                        })}
                    >
                        <span className="n2o-file-uploader-file-name">{file.name}</span>
                        {file.link && (
                            <i className=" n2o-file-uploader-external-link fa fa-external-link" />
                        )}
                    </a>
                    {(!isEmpty(file.error) || !isEmpty(file.response)) && (
                        <Tooltip
                            isOpen={this.state.tooltipOpen}
                            target={`tooltip-${file.id}`}
                            toggle={this.toggle}
                        >
                            {file.response || file.error}
                        </Tooltip>
                    )}
                    <span className="n2o-file-uploader-item-size">
                        {showSize && <span>{convertSize(file.size)}</span>}
                        {!disabled && !loading && (
                            <i
                                onClick={() => onRemove(index, file.id)}
                                className="n2o-file-uploader-remove fa fa-times ml-2"
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
}

FileUploaderItem.propTypes = {
    file: PropTypes.object,
    percentage: PropTypes.number,
    statusBarColor: PropTypes.string,
    onRemove: PropTypes.func,
    showSize: PropTypes.bool,
    disabled: PropTypes.bool,
    error: PropTypes.bool,
    status: PropTypes.number,
    index: PropTypes.number,
    loading: PropTypes.bool,
}

FileUploaderItem.defaultProps = {
    statusBarColor: 'success',
}

export default FileUploaderItem
