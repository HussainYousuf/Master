<?php
    function redirect($link){
        header("Location: $link");
        exit();
    }

    function show_msg($title,$msg){
        echo <<<END
        <div class="modal">
  <div class="modal-background"></div>
  <div class="modal-card">
    <header class="modal-card-head">
      <p class="modal-card-title">$title</p>
    </header>
    <section class="modal-card-body">
      $msg
    </section>
    <footer class="modal-card-foot">
      <button class="button is-dark is-fullwidth close-modal">Close</button>
    </footer>
  </div>
</div>
    
END;
        echo <<<END
            <script>  
            $(".modal").addClass("is-active");
            $(".modal-background").click(function() {
                $(".modal").removeClass("is-active");
            });
            $(".close-modal").click(function() {
                $(".modal").removeClass("is-active");
            });
        </script>
END;
    }



    function toast($msg,$tag){
      echo <<<END
            <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
            <script>
                swal("$msg", "", "$tag");
            </script>
END;
    }

?>