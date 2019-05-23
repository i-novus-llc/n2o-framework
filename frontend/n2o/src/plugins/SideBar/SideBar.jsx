import React from 'react';
import UserBox from '../../components/snippets/UserBox/UserBox';
import { compose } from 'recompose';

function SideBar(props) {
  return (
    <aside className="n2o-sidebar">
      <div className="n2o-sidebar__nav-brand n2o-nav-brand d-flex justify-content-center">
        <a href="/">
          {/*<img*/}
          {/*className="n2o-nav-brand__icon"*/}
          {/*src="https://jira.i-novus.ru/secure/projectavatar?pid=10138&avatarId=11202"*/}
          {/*alt=""*/}
          {/*width="30"*/}
          {/*height="30"*/}
          {/*/>*/}
          <span className="n2o-nav-brand__text">N2O Framework</span>
        </a>
      </div>
      <UserBox
        image="http://tiolly.by/img/empty_user.png"
        title="Александр Петров"
        subTitle="17.01.1987 * Москва"
      >
        <a href="">dsa</a>
        <a href="">dsa</a>
        <a href="">dsa</a>
      </UserBox>
    </aside>
  );
}

export default SideBar;
