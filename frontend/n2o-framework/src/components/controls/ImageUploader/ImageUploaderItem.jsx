import React from 'react';
import PropTypes from 'prop-types';

import isEmpty from 'lodash/isEmpty';
import isUndefined from 'lodash/isUndefined';
import get from 'lodash/get';

import cn from 'classnames';

import Tooltip from 'reactstrap/lib/Tooltip';
import Modal from 'reactstrap/lib/Modal';

import { convertSize } from '../FileUploader/utils';
import Spinner from '../../snippets/Spinner/Spinner';

class ImageUploaderItem extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      tooltipOpen: false,
      modalOpen: false,
    };

    this.toggle = this.toggle.bind(this);
  }
  toggle() {
    this.setState({
      tooltipOpen: !this.state.tooltipOpen,
    });
  }

  modalOpen() {
    this.setState({ modalOpen: true });
  }

  modalClose() {
    this.setState({ modalOpen: false });
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
      listType = 'image',
    } = this.props;

    const cardType = listType === 'card';
    const imageType = listType === 'image';
    const withInformation = showSize || showName;

    const imgSrc = isUndefined(file.link)
      ? URL.createObjectURL(file)
      : get(file, 'link');

    return (
      <div className="n2o-image-uploader-files-item">
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
              'n2o-file-uploader-item-error': file.error,
              'single-img': imageType,
            })}
          >
            <div
              className={cn('n2o-image-uploader__watch', {
                'single-img': imageType,
              })}
            >
              <div className="n2o-image-uploader__watch--icons-container">
                {lightBox && (
                  <span>
                    <i
                      onClick={() => this.modalOpen()}
                      className="n2o-image-uploader__watch--eye fa fa-eye"
                    />
                  </span>
                )}
                <span>
                  <i
                    onClick={() => onRemove(index, file.id)}
                    className="n2o-image-uploader__watch--trash fa fa-trash"
                  />
                </span>
              </div>
            </div>
            <img className="n2o-image-uploader--img" src={imgSrc} alt="some" />
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
          isOpen={this.state.modalOpen}
          backdrop={true}
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
              alt="some"
            />
          </div>
        </Modal>
      </div>
    );
  }
}

ImageUploaderItem.propTypes = {
  file: PropTypes.object,
  onRemove: PropTypes.func,
  showSize: PropTypes.bool,
  disabled: PropTypes.bool,
  error: PropTypes.bool,
  status: PropTypes.number,
  index: PropTypes.number,
  loading: PropTypes.bool,
};

ImageUploaderItem.defaultProps = {
  statusBarColor: 'success',
};

export default ImageUploaderItem;
