import React from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import isUndefined from 'lodash/isUndefined'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'
import omit from 'lodash/omit'
import cn from 'classnames'
import Tooltip from 'reactstrap/lib/Tooltip'
import Modal from 'reactstrap/lib/Modal'

import { convertSize } from '../FileUploader/utils'
import Spinner from '../../snippets/Spinner/Spinner'

class ImageUploaderItem extends React.Component {
    state = {
        tooltipOpen: false,
        modalOpen: false,
    }

    toggle = () => {
        const { tooltipOpen } = this.state

        this.setState({
            tooltipOpen: !tooltipOpen,
        })
    }

    modalOpen() {
        this.setState({ modalOpen: true })
    }

    modalClose() {
        this.setState({ modalOpen: false })
    }

    render() {
        const {
            file,
            onRemove,
            showSize,
            showName,
            index,
            loading,
            lightBox,
            listType,
            customUploaderSize,
            showTooltip,
            canDelete,
            shape,
        } = this.props
        const { tooltipOpen, modalOpen } = this.state

        const cardType = listType === 'card'
        const imageType = listType === 'image'
        const withInformation = showSize || showName
        const shapeCircle = isEqual(shape, 'circle')
        const cardWithShapeCircle = cardType && shapeCircle

        let imgSrc

        if (!isUndefined(file.error)) {
            imgSrc = ''
        } else if (isUndefined(file.link)) {
            imgSrc = URL.createObjectURL(file)
        } else {
            imgSrc = get(file, 'link')
        }

        const modifyStyle = size => (cardType && !shapeCircle ? omit(size, 'maxWidth', 'width') : size)

        const modifyContainerStyle = size => (cardWithShapeCircle ? omit(size, 'maxWidth', 'width') : size)

        return (
            <div
                className="n2o-image-uploader-files-item"
                style={modifyContainerStyle(customUploaderSize)}
            >
                <span
                    className={cn('n2o-file-uploader-files-item-info', {
                        'with-info': cardType && withInformation,
                    })}
                >
                    <a
                        title={file.name}
                        target="_blank"
                        id={`tooltip-${file.id}`}
                        className={cn('n2o-image-uploader-link', {
                            'n2o-image-uploader-item-error': file.error,
                            'single-img': imageType,
                            'n2o-image-uploader-link--shape-circle': shapeCircle,
                        })}
                        style={modifyStyle(customUploaderSize)}
                    >
                        <div
                            className={cn('n2o-image-uploader__watch', {
                                'single-img': imageType,
                                'n2o-image-uploader__watch--shape-circle': shapeCircle,
                            })}
                            style={modifyStyle(customUploaderSize)}
                        >
                            <div className="n2o-image-uploader__watch--icons-container">
                                {lightBox && isUndefined(file.error) && (
                                    <span>
                                        <i
                                            onClick={() => this.modalOpen()}
                                            className="n2o-image-uploader__watch--eye fa fa-eye"
                                        />
                                    </span>
                                )}
                                {canDelete && (
                                    <span>
                                        <i
                                            onClick={() => onRemove(index, file.id)}
                                            className="n2o-image-uploader__watch--trash fa fa-trash"
                                        />
                                    </span>
                                )}
                            </div>
                        </div>
                        <img
                            className={cn('n2o-image-uploader--img', {
                                'n2o-image-uploader--img--shape-circle': shapeCircle,
                            })}
                            src={imgSrc}
                            alt={!shapeCircle ? 'upload error' : ''}
                            style={omit(customUploaderSize, 'height')}
                        />
                    </a>
                    {
                        showTooltip && (!isEmpty(file.error) || !isEmpty(file.response))
                            ? (
                                <Tooltip
                                    isOpen={tooltipOpen}
                                    target={`tooltip-${file.id}`}
                                    toggle={this.toggle}
                                >
                                    {file.response || file.error}
                                </Tooltip>
                            )
                            : null
                    }
                    <div className="n2o-image-uploader-img-info">
                        {cardType && showName && (
                            <span className="n2o-image-uploader-img-info__file-name">
                                {file.name}
                            </span>
                        )}
                        <span className="n2o-image-uploader-img-info__file-size">
                            {cardType && showSize && <span>{convertSize(file.size)}</span>}
                            {loading && <Spinner className="ml-2" type="inline" size="sm" />}
                        </span>
                    </div>
                </span>
                <Modal
                    isOpen={modalOpen}
                    backdrop
                    centered
                    toggle={() => this.modalClose()}
                    className="n2o-image-uploader__modal"
                >
                    <div className="n2o-image-uploader__modal--body">
                        <i
                            onClick={() => this.modalClose()}
                            className="n2o-image-uploader__modal--icon-close fa fa-times"
                        />
                        <img
                            className="n2o-image-uploader__modal--image"
                            src={imgSrc}
                            alt="upload error"
                        />
                    </div>
                </Modal>
            </div>
        )
    }
}

ImageUploaderItem.propTypes = {
    file: PropTypes.object,
    onRemove: PropTypes.func,
    showSize: PropTypes.bool,
    showName: PropTypes.bool,
    index: PropTypes.number,
    loading: PropTypes.bool,
    lightBox: PropTypes.bool,
    listType: PropTypes.string,
    customUploaderSize: PropTypes.number,
    showTooltip: PropTypes.bool,
    canDelete: PropTypes.bool,
    shape: PropTypes.string,
}

ImageUploaderItem.defaultProps = {
    listType: 'image',
}

export default ImageUploaderItem
