import React from 'react';
import { mount } from 'enzyme';
import sinon from 'sinon';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import ButtonUploader from './ButtonUploader';
import DropZone from './DropZone';

const mock = new MockAdapter(axios);

const props = {
  label: 'Загрузчик файлов',
  className: 'custom-class',
  name: 'avatar',
  disabled: false,
  uploadUrl: '/n2o/data',
  deleteUrl: '/n2o/data/delete',
  autoUpload: true,
  showSize: true,
  multi: false,
  valueFieldId: 'customId',
  labelFieldId: 'customName',
  statusFieldId: 'customStatus',
  sizeFieldId: 'customSize',
  responseFieldId: 'customResponse',
  urlFieldId: 'customLink',
};

const setupButton = (propsOverride, context = {}) => {
  return mount(<ButtonUploader {...props} {...propsOverride} />, context);
};

const setupDropZone = propsOverride => {
  return mount(<DropZone {...props} {...propsOverride} />);
};

describe('FileUploader Тесты', () => {
  describe('Тесты ButtonUploader', () => {
    it('Меняет состояние при update props value', () => {
      const button = setupButton();
      expect(button.state()).toEqual({
        files: [],
        imgError: {},
        imgFiles: [],
      });
      button.setProps({
        value: {
          customId: 1,
          customName: 'filename.test',
          customStatus: 'success',
          customSize: '1024',
          customLink: '/test',
        },
      });
      expect(button.state()).toEqual({
        files: [
          {
            id: 1,
            name: 'filename.test',
            status: 'success',
            size: '1024',
            response: undefined,
            link: '/test',
          },
        ],
        imgError: {},
        imgFiles: [],
      });
    });

    it('Отрисовывается', () => {
      const button = setupButton();
      expect(button.find('.n2o-button-uploader').exists()).toEqual(true);
    });

    it('Происходит маппинг', () => {
      const button = setupButton({
        valueFieldId: 'ID',
        labelFieldId: 'label',
        sizeFieldId: 'size',
        files: [
          {
            ID: 123,
            label: 'test',
            size: 329184,
          },
        ],
        imgError: {},
        imgFiles: [],
      });

      expect(button.state()).toEqual({
        files: [
          {
            id: 123,
            name: 'test',
            size: 329184,
          },
        ],
        imgError: {},
        imgFiles: [],
      });
    });

    it('Отрисовался item файла', () => {
      const button = setupButton({
        valueFieldId: 'ID',
        labelFieldId: 'label',
        sizeFieldId: 'size',
        files: [
          {
            ID: 123,
            label: 'test',
            size: 329184,
          },
        ],
      });
      expect(button.find('.n2o-file-uploader-files-item').exists()).toEqual(
        true
      );
    });

    describe('Проверка режимов', () => {
      it('disabled', () => {
        const button = setupButton({
          files: [
            {
              customId: 123,
              customName: 'test.png',
              customSize: 12341234,
            },
          ],
          disabled: true,
        });
        expect(button.find('.n2o-file-uploader-files-item').length).toEqual(1);
        expect(button.find('.n2o-file-uploader-remove').exists()).toEqual(
          false
        );
        expect(button.find('.n2o-file-uploader-control').exists()).toEqual(
          false
        );
      });

      it('Single', () => {
        const button = setupButton({
          files: [
            {
              customId: 123,
              customName: 'test.png',
              customSize: 12341234,
            },
          ],
          multi: false,
        });
        expect(button.find('.n2o-file-uploader-remove').exists()).toEqual(true);
      });

      it('Multi', () => {
        const button = setupButton({
          files: [
            {
              customId: 1,
              customName: 'first.jpg',
              customSize: '231321',
            },
            {
              customId: 2,
              customName: 'second.jpg',
              customSize: '897978',
            },
          ],
          multi: true,
        });
        expect(button.find('.n2o-file-uploader-control').exists()).toEqual(
          true
        );
        expect(button.find('.n2o-file-uploader-files-item').length).toEqual(2);
      });
    });
  });

  describe('Тесты DropZone', () => {
    it('Отрисовывается', () => {
      const dropZone = setupDropZone();
      expect(dropZone.find('.n2o-drop-zone').exists()).toEqual(true);
    });

    it('Происходит маппинг', () => {
      const dropZone = setupDropZone({
        valueFieldId: 'ID',
        labelFieldId: 'label',
        sizeFieldId: 'size',
        files: [
          {
            ID: 123,
            label: 'test',
            size: 329184,
          },
        ],
        imgError: {},
        imgFiles: [],
      });
      expect(dropZone.state()).toEqual({
        files: [
          {
            id: 123,
            name: 'test',
            size: 329184,
          },
        ],
        imgError: {},
        imgFiles: [],
      });
    });

    it('Отрисовался item файла', () => {
      const dropZone = setupDropZone({
        valueFieldId: 'ID',
        labelFieldId: 'label',
        sizeFieldId: 'size',
        files: [
          {
            ID: 123,
            label: 'test',
            size: 329184,
          },
        ],
      });
      expect(dropZone.find('.n2o-file-uploader-files-item').exists()).toEqual(
        true
      );
    });

    describe('Проверка режимов', () => {
      it('disabled', () => {
        const dropZone = setupDropZone({
          files: [
            {
              customId: 123,
              customName: 'test.png',
              customSize: 12341234,
            },
          ],
          disabled: true,
        });
        expect(dropZone.find('.n2o-file-uploader-files-item').length).toEqual(
          1
        );
        expect(dropZone.find('.n2o-file-uploader-remove').exists()).toEqual(
          false
        );
        expect(dropZone.find('.n2o-file-uploader-control').exists()).toEqual(
          false
        );
      });

      it('Single', () => {
        const dropZone = setupDropZone({
          files: [
            {
              customId: 123,
              customName: 'test.png',
              customSize: 12341234,
            },
          ],
          multi: false,
        });
        expect(dropZone.find('.n2o-file-uploader-remove').exists()).toEqual(
          true
        );
      });

      it('Multi', () => {
        const dropZone = setupDropZone({
          files: [
            {
              customId: 1,
              customName: 'first.jpg',
              customSize: '231321',
            },
            {
              customId: 2,
              customName: 'second.jpg',
              customSize: '897978',
            },
          ],
          multi: true,
        });
        expect(dropZone.find('.n2o-file-uploader-control').exists()).toEqual(
          true
        );
        expect(dropZone.find('.n2o-file-uploader-files-item').length).toEqual(
          2
        );
      });

      it('AutoUpload', () => {
        const dropZone = setupDropZone({
          autoUpload: false,
        });
        expect(dropZone.find('.n2o-drop-zone-save-btn').exists()).toEqual(true);
      });
    });
  });

  it('Проверка отправки запросов', () => {
    mock.onPost('/n2o/data').reply(200, {
      customId: undefined,
      customName: 'файл с сервера.png',
      customStatus: 'success',
      customResponse: 'response',
      customLink: 'google.com',
    });

    const button = setupButton({
      files: [
        {
          customId: undefined,
          customName: 'first.jpg',
          customSize: '231321',
        },
      ],
    });
    button.instance().startUpload([
      {
        customId: undefined,
        customName: 'first.jpg',
        customSize: '231321',
      },
    ]);
    button.update();
    expect(button.state().files[0]).toEqual({
      id: undefined,
      name: 'first.jpg',
      status: undefined,
      size: '231321',
      response: undefined,
      link: undefined,
    });
  });

  it('Проверка обработки ответа от сервера', () => {
    const button = setupButton({
      files: [
        {
          customId: 2,
          customName: 'test.png',
          customSize: 1,
        },
      ],
    });
    button.instance().onUpload(2, {
      status: 201,
      data: {
        customId: 2,
        customName: 'newFile.png',
        customSize: 100,
        customLink: 'link',
        customStatus: 'success',
      },
    });
    button.update();
    setImmediate(() => {
      expect(button.state().files).toEqual([
        {
          id: 2,
          name: 'newFile.png',
          size: 100,
          link: 'link',
          loading: false,
          status: 'success',
          response: undefined,
        },
      ]);
    }, 0);
  });

  it('Проверка удаления файла', () => {
    const onBlur = sinon.spy();
    const button = setupButton({
      files: [
        {
          customId: 1,
          name: 'test.name',
          size: 1,
        },
      ],
      onBlur,
    });
    button.instance().handleRemove(0);
    expect(button.state().files).toEqual([]);
    expect(onBlur.calledOnce).toBeTruthy();
  });

  it('Проверка onChange', () => {
    const button = setupButton({
      onChange: value => value,
    });
    expect(button.props().onChange('test')).toEqual('test');
  });

  it('Проверка url placeholder', () => {
    const button = setupButton(
      {
        uploadUrl: "`'name:' + name`",
      },
      {
        context: {
          _reduxForm: {
            resolveModel: {
              name: 'test',
            },
          },
        },
      }
    );
    expect(button.instance().resolveUrl("`'name: ' + name`")).toEqual(
      'name: test'
    );
  });
});
