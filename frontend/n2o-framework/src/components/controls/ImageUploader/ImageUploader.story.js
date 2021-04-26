import React from 'react'
import { storiesOf } from '@storybook/react'
import withForm from 'N2oStorybook/decorators/withForm'
import uniqueId from 'lodash/uniqueId'
import axios from 'axios'
import MockAdapter from 'axios-mock-adapter'

import metadata from './ImageUploader.meta.json'
import ImageUploaderDropZone from './ImageUploader'

const mockAxios = new MockAdapter(axios, { delayResponse: 500 })
const form = withForm({ src: 'DropZone' })
const stories = storiesOf('Контролы/Загрузчик изображений', module)

stories
    .add(
        'Загрузка изображений (single, image)',
        () => {
            mockAxios.onPost('/n2o/data').reply(config => [
                200,
                {
                    customId: `file_${uniqueId()}`,
                    customName: config.data.get('avatar').name,
                    customSize: config.data.get('avatar').size,
                    customStatus: 'success',
                    customResponse: 'File uploaded success!',
                    customLink: 'https://www.google.com',
                },
            ])

            return <ImageUploaderDropZone {...metadata} lightBox={false} />
        },
        {
            info: {
                text: `
      Компонент 'Загрузчик изображений'
      ~~~js
      import DropZone from 'n2o-framework/lib/components/controls/ImageUploader/ImageUploaderDropZone';
      
      <ImageUploaderDropZone 
       label="Загрузите изображение"
       className="custom-class"
       requestParam= "avatar"
       disabled= false,
       uploadUrl= "/n2o/data"
       deleteUrl= "/n2o/data/delete"
       showSize="true"
       showName="true"
       multi="false"
       autoUpload="false"
       listType="image"
       lightBox="false"
       valueFieldId="customId"
       labelFieldId="customName"
       statusFieldId="customStatus"
       sizeFieldId="customSize"
       responseFieldId="customResponse"
       urlFieldId="customLink"
       icon="fa fa-plus"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Загрузка изображений (single, card)',
        () => {
            mockAxios.onPost('/n2o/data').reply(config => [
                200,
                {
                    customId: `file_${uniqueId()}`,
                    customName: config.data.get('avatar').name,
                    customSize: config.data.get('avatar').size,
                    customStatus: 'success',
                    customResponse: 'File uploaded success!',
                    customLink: 'https://www.google.com',
                },
            ])

            return (
                <ImageUploaderDropZone
                    {...metadata}
                    lightBox={false}
                    listType="card"
                />
            )
        },
        {
            info: {
                text: `
      Компонент 'Загрузчик изображений'
      ~~~js
      import DropZone from 'n2o-framework/lib/components/controls/ImageUploader/ImageUploaderDropZone';
      
      <ImageUploaderDropZone 
       label="Загрузите изображение"
       className="custom-class"
       requestParam= "avatar"
       disabled= false,
       uploadUrl= "/n2o/data"
       deleteUrl= "/n2o/data/delete"
       showSize="true"
       showName="true"
       multi="false"
       autoUpload="false"
       listType="image"
       lightBox="false"
       valueFieldId="customId"
       labelFieldId="customName"
       statusFieldId="customStatus"
       sizeFieldId="customSize"
       responseFieldId="customResponse"
       urlFieldId="customLink"
       icon="fa fa-plus"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Загрузка изображений (single, image, lightBox)',
        () => {
            mockAxios.onPost('/n2o/data').reply(config => [
                200,
                {
                    customId: `file_${uniqueId()}`,
                    customName: config.data.get('avatar').name,
                    customSize: config.data.get('avatar').size,
                    customStatus: 'success',
                    customResponse: 'File uploaded success!',
                    customLink: 'https://www.google.com',
                },
            ])

            return <ImageUploaderDropZone {...metadata} />
        },
        {
            info: {
                text: `
      Компонент 'Загрузчик изображений'
      ~~~js
      import DropZone from 'n2o-framework/lib/components/controls/ImageUploader/ImageUploaderDropZone';
      
      <ImageUploaderDropZone 
       label="Загрузите изображение"
       className="custom-class"
       requestParam= "avatar"
       disabled= false,
       uploadUrl= "/n2o/data"
       deleteUrl= "/n2o/data/delete"
       showSize="true"
       showName="true"
       multi="false"
       autoUpload="false"
       listType="image"
       lightBox="false"
       valueFieldId="customId"
       labelFieldId="customName"
       statusFieldId="customStatus"
       sizeFieldId="customSize"
       responseFieldId="customResponse"
       urlFieldId="customLink"
       icon="fa fa-plus"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Загрузка изображений (single, card, lightBox)',
        () => {
            mockAxios.onPost('/n2o/data').reply(config => [
                200,
                {
                    customId: `file_${uniqueId()}`,
                    customName: config.data.get('avatar').name,
                    customSize: config.data.get('avatar').size,
                    customStatus: 'success',
                    customResponse: 'File uploaded success!',
                    customLink: 'https://www.google.com',
                },
            ])

            return <ImageUploaderDropZone {...metadata} listType="card" />
        },
        {
            info: {
                text: `
      Компонент 'Загрузчик изображений'
      ~~~js
      import DropZone from 'n2o-framework/lib/components/controls/ImageUploader/ImageUploaderDropZone';
      
      <ImageUploaderDropZone 
       label="Загрузите изображение"
       className="custom-class"
       requestParam= "avatar"
       disabled= false,
       uploadUrl= "/n2o/data"
       deleteUrl= "/n2o/data/delete"
       showSize="true"
       showName="true"
       multi="false"
       autoUpload="false"
       listType="image"
       lightBox="false"
       valueFieldId="customId"
       labelFieldId="customName"
       statusFieldId="customStatus"
       sizeFieldId="customSize"
       responseFieldId="customResponse"
       urlFieldId="customLink"
       icon="fa fa-plus"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Загрузка изображений (multi, image)',
        () => {
            mockAxios.onPost('/n2o/data').reply(config => [
                200,
                {
                    customId: `file_${uniqueId()}`,
                    customName: config.data.get('avatar').name,
                    customSize: config.data.get('avatar').size,
                    customStatus: 'success',
                    customResponse: 'File uploaded success!',
                    customLink: 'https://www.google.com',
                },
            ])

            return <ImageUploaderDropZone {...metadata} multi />
        },
        {
            info: {
                text: `
      Компонент 'Загрузчик изображений'
      ~~~js
      import DropZone from 'n2o-framework/lib/components/controls/ImageUploader/ImageUploaderDropZone';
      
      <ImageUploaderDropZone 
       label="Загрузите изображение"
       className="custom-class"
       requestParam= "avatar"
       disabled= false,
       uploadUrl= "/n2o/data"
       deleteUrl= "/n2o/data/delete"
       showSize="true"
       showName="true"
       multi="false"
       autoUpload="false"
       listType="image"
       lightBox="false"
       valueFieldId="customId"
       labelFieldId="customName"
       statusFieldId="customStatus"
       sizeFieldId="customSize"
       responseFieldId="customResponse"
       urlFieldId="customLink"
       icon="fa fa-plus"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Загрузка изображений (single, card, lightBox)',
        () => {
            mockAxios.onPost('/n2o/data').reply(config => [
                200,
                {
                    customId: `file_${uniqueId()}`,
                    customName: config.data.get('avatar').name,
                    customSize: config.data.get('avatar').size,
                    customStatus: 'success',
                    customResponse: 'File uploaded success!',
                    customLink: 'https://www.google.com',
                },
            ])

            return <ImageUploaderDropZone {...metadata} listType="card" />
        },
        {
            info: {
                text: `
      Компонент 'Загрузчик изображений'
      ~~~js
      import DropZone from 'n2o-framework/lib/components/controls/ImageUploader/ImageUploaderDropZone';
      
      <ImageUploaderDropZone 
       label="Загрузите изображение"
       className="custom-class"
       requestParam= "avatar"
       disabled= false,
       uploadUrl= "/n2o/data"
       deleteUrl= "/n2o/data/delete"
       showSize="true"
       showName="true"
       multi="false"
       autoUpload="false"
       listType="image"
       lightBox="false"
       valueFieldId="customId"
       labelFieldId="customName"
       statusFieldId="customStatus"
       sizeFieldId="customSize"
       responseFieldId="customResponse"
       urlFieldId="customLink"
       icon="fa fa-plus"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Загрузка изображений (multi, card)',
        () => {
            mockAxios.onPost('/n2o/data').reply(config => [
                200,
                {
                    customId: `file_${uniqueId()}`,
                    customName: config.data.get('avatar').name,
                    customSize: config.data.get('avatar').size,
                    customStatus: 'success',
                    customResponse: 'File uploaded success!',
                    customLink: 'https://www.google.com',
                },
            ])

            return (
                <ImageUploaderDropZone {...metadata} multi listType="card" />
            )
        },
        {
            info: {
                text: `
      Компонент 'Загрузчик изображений'
      ~~~js
      import DropZone from 'n2o-framework/lib/components/controls/ImageUploader/ImageUploaderDropZone';
      
      <ImageUploaderDropZone 
       label="Загрузите изображение"
       className="custom-class"
       requestParam= "avatar"
       disabled= false,
       uploadUrl= "/n2o/data"
       deleteUrl= "/n2o/data/delete"
       showSize="true"
       showName="true"
       multi="false"
       autoUpload="false"
       listType="image"
       lightBox="false"
       valueFieldId="customId"
       labelFieldId="customName"
       statusFieldId="customStatus"
       sizeFieldId="customSize"
       responseFieldId="customResponse"
       urlFieldId="customLink"
       icon="fa fa-plus"
       />
      ~~~
      `,
            },
        },
    )
