import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty, isEqual, isArray } from 'lodash';
import merge from 'deepmerge';
import { post, deleteFile } from './utils';
import { id } from '../../../utils/id';
import evalExpression, { parseExpression } from '../../../utils/evalExpression';

const FileUploaderControl = WrappedComponent => {
  class ReturnedComponent extends React.Component {
    constructor(props) {
      super(props);

      this.state = {
        files: props.files || []
      };
      this.requests = {};

      this.handleDrop = this.handleDrop.bind(this);
      this.handleRemove = this.handleRemove.bind(this);
      this.handleChange = this.handleChange.bind(this);
      this.startUpload = this.startUpload.bind(this);
      this.onStartUpload = this.onStartUpload.bind(this);
      this.getUploaderProps = this.getUploaderProps.bind(this);
      this.resolveUrl = this.resolveUrl.bind(this);
      this.onDragEnter = this.onDragEnter.bind(this);
      this.onDragLeave = this.onDragLeave.bind(this);
      this.onError = this.onError.bind(this);
    }

    componentDidMount() {
      const { mapper, value } = this.props;
      this.setState({
        files: mapper ? mapper(value) : this.mapFiles(!isEmpty(value) ? value : this.state.files)
      });
    }

    mapFiles(files) {
      let currentFiles = [];
      if (!isArray(files)) {
        currentFiles = [files];
      } else {
        currentFiles = files;
      }
      return currentFiles.map(file => {
        return this.fileAdapter(file);
      });
    }

    fileAdapter(file) {
      const {
        valueFieldId,
        labelFieldId,
        statusFieldId,
        sizeFieldId,
        responseFieldId,
        urlFieldId
      } = this.props;
      return {
        id: file[valueFieldId],
        name: file[labelFieldId],
        status: file[statusFieldId],
        size: file[sizeFieldId],
        response: file[responseFieldId],
        link: file[urlFieldId]
      };
    }

    /**
     * Получение Url из expression
     * @returns {*}
     */
    resolveUrl(url) {
      const expression = parseExpression(url);
      if (!expression) {
        return url;
      }
      const resolveModel = this.context._reduxForm.resolveModel;
      return evalExpression(expression, resolveModel);
    }

    /**
     * Return props
     */
    getUploaderProps() {
      return {
        ...this.props,
        files: this.state.files,
        uploading: this.state.uploading,
        requests: this.requests,
        multiple: this.props.multi,
        uploaderClass: this.state.uploaderClass,
        onFocus: () => {},
        onBlur: () => {},
        onDrop: this.handleDrop,
        onDragLeave: this.onDragLeave,
        onDragEnter: this.onDragEnter,
        onRemove: this.handleRemove,
        onStartUpload: this.onStartUpload
      };
    }

    /**
     * Загрузка файлов в state
     * @param files
     */
    handleDrop(files) {
      this.setState({
        files: [
          ...this.state.files,
          ...files.map(file => {
            file.id = id();
            file.percentage = 0;
            return file;
          })
        ],
        uploaderClass: null
      });
      if (this.props.autoUpload) {
        this.startUpload(files);
      }
    }

    /**
     * Удаление из стейта
     * @param index
     * @param id
     */
    handleRemove(index, id) {
      const { value = [], multi, valueFieldId, onChange, deleteUrl } = this.props;
      if (deleteUrl) {
        deleteFile(this.resolveUrl(deleteUrl), id);
      }
      const newFiles = this.state.files.slice();
      newFiles.splice(index, 1);
      this.setState({
        files: [...newFiles]
      });
      onChange(multi ? value.filter(f => f[valueFieldId] !== id) : {});
    }

    /**
     * Изменение компонента
     */
    handleChange(newFile) {
      const { value, multi, onChange } = this.props;
      onChange(multi ? [...value, newFile] : newFile);
    }

    /**
     * Start upload files
     * @param files
     */
    startUpload(files) {
      const { labelFieldId, sizeFieldId, name, uploadUrl } = this.props;
      const url = this.resolveUrl(uploadUrl);
      files.map(file => {
        if (!this.requests[file.id]) {
          const onProgress = this.onProgress.bind(this, file.id);
          const onUpload = this.onUpload.bind(this, file.id);
          if (labelFieldId !== 'name') {
            file[labelFieldId] = file.name;
          }
          if (sizeFieldId !== 'size') {
            file[sizeFieldId] = file.size;
          }
          this.setState({
            uploading: {
              ...this.state.uploading,
              [file.id]: true
            }
          });
          const formData = new FormData();
          formData.append(name, file);
          this.requests[file.id] = post(url, formData, onProgress, onUpload);
        }
      });
    }

    /**
     * Change upload progress
     * @param id
     * @param event
     */
    onProgress(id, event) {
      if (event.lengthComputable) {
        this.onLoading(event.loaded / event.total, id);
      }
    }

    /**
     * Loading event
     * @param percentage
     * @param id
     */
    onLoading(percentage, id) {
      const { files } = this.state;
      this.setState({
        files: [
          ...files.map(file => {
            if (file.id === id) {
              file.percentage = percentage;
            }
            return file;
          })
        ]
      });
    }

    /**
     * Call upload function
     */
    onStartUpload() {
      this.startUpload(this.state.files);
    }

    /**
     * Upload event
     * @param id
     * @param xhr
     */
    onUpload(id, xhr) {
      if (xhr.status < 200 || xhr.status >= 300) {
        this.onError(id, xhr.statusText, xhr.status);
      }
      if (xhr.readyState === XMLHttpRequest.DONE && xhr.status >= 200 && xhr.status < 300) {
        const file = JSON.parse(xhr.responseText);
        const size = xhr.getResponseHeader('Content-Length');
        this.setState({
          files: [
            ...this.state.files.map(item => {
              if (item.id === id) {
                return {
                  ...this.fileAdapter(file),
                  size,
                  loading: false
                };
              }
              return item;
            })
          ],
          uploading: {
            ...this.state.uploading,
            [id]: false
          }
        });
        this.requests[id] = undefined;
        this.handleChange(file);
      }
    }

    onError(id, error, status) {
      const uploading = this.state.uploading;
      if (uploading) {
        uploading[id] = false;
      }
      this.setState({
        uploading
      });
      this.setState({
        ...this.state.files.map(file => {
          if (file.id === id) {
            file.error = error || status;
          }
        })
      });
    }

    onDragEnter() {
      this.setState({
        uploaderClass: 'n2o-file-uploader-event-drag-enter'
      });
    }

    onDragLeave() {
      this.setState({
        uploaderClass: null
      });
    }

    render() {
      return <WrappedComponent {...this.getUploaderProps()} />;
    }
  }

  ReturnedComponent.contextTypes = {
    _reduxForm: PropTypes.string
  };

  ReturnedComponent.defaultProps = {
    label: 'Загрузить файл',
    name: 'file',
    visible: true,
    icon: 'fa fa-upload',
    statusBarColor: 'success',
    multi: true,
    disabled: false,
    autoUpload: true,
    showSize: true,
    value: [],
    onChange: value => {}
  };

  ReturnedComponent.propTypes = {
    valueFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    statusFieldId: PropTypes.string,
    sizeFieldId: PropTypes.string,
    responseFieldId: PropTypes.string,
    urlFieldId: PropTypes.string,
    uploadUrl: PropTypes.string,
    deleteUrl: PropTypes.string,
    multi: PropTypes.bool,
    files: PropTypes.arrayOf(PropTypes.object),
    autoUpload: PropTypes.bool,
    maxSize: PropTypes.number,
    minSize: PropTypes.number,
    label: PropTypes.string,
    name: PropTypes.string,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
    icon: PropTypes.string,
    statusBarColor: PropTypes.string,
    saveBtnStyle: PropTypes.object,
    showSize: PropTypes.bool,
    onChange: PropTypes.func,
    className: PropTypes.string,
    mapper: PropTypes.func,
    children: PropTypes.oneOf(PropTypes.func, PropTypes.node)
  };

  return ReturnedComponent;
};

export default FileUploaderControl;
