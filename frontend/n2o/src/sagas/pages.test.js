import React from 'react';
import {
  watcherDefaultModels,
  flowDefaultModels,
  compareAndResolve,
} from './pages';
import { channel } from 'redux-saga';
import {
  select,
  race,
  call,
  take,
  put,
  actionChannel,
} from 'redux-saga/effects';
import { RESET } from '../constants/pages';
import {
  SET,
  COPY,
  SYNC,
  REMOVE,
  UPDATE,
  UPDATE_MAP,
} from '../constants/models';
import { combineModels } from '../actions/models';

const resolveModelsValue = {
  "resolve['page_main_create'].name": {
    value: 'Test',
  },
};

const resolveModelsLink = {
  "resolve['page_main_create'].name": {
    link: "filter['field'].id",
  },
};

const resolveModelsLinkAndValue = {
  "resolve['page_main_create'].name": {
    link: "filter['field']",
    value: '`id`',
  },
};

describe('Сага для для наблюдения за изменением модели', () => {
  it('Проверяем watcher дефолтных моделей', () => {
    const config = { 'a.b.c': { value: 'test' } };
    const gen = watcherDefaultModels(config);
    expect(gen.next().value).toEqual(
      race([call(flowDefaultModels, config), take(RESET)])
    );
    expect(gen.next().done).toEqual(true);
  });
  it('Проверяем flowDefaultModels - выход без конфига', () => {
    const gen = flowDefaultModels();
    expect(gen.next().value).toEqual(false);
    expect(gen.next().done).toEqual(true);
  });
  it('Проверяем flowDefaultModels - только init значение, без подписи', () => {
    const config = { 'a.b.c': { value: 'test' } };
    const state = { a: { b: { c: 1 } } };
    const gen = flowDefaultModels(config);
    expect(gen.next().value).toEqual(select());
    expect(gen.next(state).value).toEqual(
      call(compareAndResolve, config, state)
    );
    expect(gen.next(compareAndResolve(config, state)).value).toEqual(
      put(combineModels(compareAndResolve(config, state)))
    );
    expect(gen.next().done).toEqual(true);
  });
  it('Проверяем flowDefaultModels - observe без link', () => {
    const config = { 'a.b.c': { value: 'test', observe: true } };
    const gen = flowDefaultModels(config);
    gen.next();
    gen.next();
    expect(gen.next().done).toEqual(true);
  });
  it('Проверяем flowDefaultModels - observe', () => {
    const config = { 'a.b.c': { value: 'test', link: 'z.x.c', observe: true } };
    const state = { z: { x: { c: 1 } } };
    const gen = flowDefaultModels(config);
    gen.next();
    gen.next();
    const mockChan = channel();
    expect(gen.next().value).toEqual(
      actionChannel([SET, COPY, SYNC, REMOVE, UPDATE, UPDATE_MAP])
    );
    expect(gen.next(mockChan).value).toEqual(select());
    expect(gen.next().value).toEqual(take(mockChan));
    expect(gen.next().value).toEqual(select());
    expect(gen.next(state).value).toEqual(
      call(compareAndResolve, config, state)
    );
    expect(gen.next(compareAndResolve(config, state)).value).toEqual(
      put(combineModels(compareAndResolve(config, state)))
    );
  });
  it('Проверка compareAndResolve если модели в стейте пустые', () => {
    expect(compareAndResolve(resolveModelsValue, {})).toEqual({
      resolve: { page_main_create: { name: 'Test' } },
    });
  });
  it('Проверка compareAndResolve если пришел только link и модель в стейте пустая', () => {
    expect(compareAndResolve(resolveModelsLink, {})).toEqual({});
  });
  it('Проверка compareAndResolve если пришел только link и есть что зарезолвить', () => {
    expect(
      compareAndResolve(resolveModelsLink, { filter: { field: { id: 1 } } })
    ).toEqual({
      resolve: { page_main_create: { name: 1 } },
    });
  });
  it('Проверка compareAndResolve если пришел пришед link и value и есть что зарезолвить', () => {
    expect(
      compareAndResolve(resolveModelsLinkAndValue, {
        filter: { field: { id: 1 } },
      })
    ).toEqual({
      resolve: { page_main_create: { name: 1 } },
    });
  });
});
