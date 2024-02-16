package com.sisifos.rickyandmortypaging3

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContent(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

/*
* Bu tür bir Event sınıfı genellikle bir olayın tek seferlik işlenmesini sağlamak için kullanılır. Özellikle, kullanıcı arayüzünde kullanılan LiveData veya benzeri veri akışı mekanizmalarında, bir olayın birden fazla kez işlenmemesi ve olayın daha önce işlenip işlenmediğinin takip edilmesi için bu tür bir sınıf kullanılabilir.
* Bu yapı, özellikle CharacterSearchPagingSource sınıfında bir hata meydana geldiğinde bu hatayı bir kereye mahsus (Event olarak) işlemek için kullanılıyor. Böylece, aynı hatanın birden fazla kez kullanıcı arayüzünde görüntülenmesi önlenmiş olur. Hatanın işlendiği yerde bu LiveData güncellenir ve kullanıcı arayüzünde bu hatayı göstermek üzere kullanılır.*/