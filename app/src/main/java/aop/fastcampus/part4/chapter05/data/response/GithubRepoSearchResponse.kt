package aop.fastcampus.part4.chapter05.data.response

import aop.fastcampus.part4.chapter05.data.entity.GithubRepoEntity

data class GithubRepoSearchResponse(
    val totalCount: Int,
    val githubRepoList: List<GithubRepoEntity>
)
